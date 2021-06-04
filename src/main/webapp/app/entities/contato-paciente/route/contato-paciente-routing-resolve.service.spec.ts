jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContatoPaciente, ContatoPaciente } from '../contato-paciente.model';
import { ContatoPacienteService } from '../service/contato-paciente.service';

import { ContatoPacienteRoutingResolveService } from './contato-paciente-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContatoPaciente routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContatoPacienteRoutingResolveService;
    let service: ContatoPacienteService;
    let resultContatoPaciente: IContatoPaciente | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContatoPacienteRoutingResolveService);
      service = TestBed.inject(ContatoPacienteService);
      resultContatoPaciente = undefined;
    });

    describe('resolve', () => {
      it('should return IContatoPaciente returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContatoPaciente = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContatoPaciente).toEqual({ id: 123 });
      });

      it('should return new IContatoPaciente if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContatoPaciente = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContatoPaciente).toEqual(new ContatoPaciente());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContatoPaciente = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContatoPaciente).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
