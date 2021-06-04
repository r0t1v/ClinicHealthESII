jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEnderecoPaciente, EnderecoPaciente } from '../endereco-paciente.model';
import { EnderecoPacienteService } from '../service/endereco-paciente.service';

import { EnderecoPacienteRoutingResolveService } from './endereco-paciente-routing-resolve.service';

describe('Service Tests', () => {
  describe('EnderecoPaciente routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EnderecoPacienteRoutingResolveService;
    let service: EnderecoPacienteService;
    let resultEnderecoPaciente: IEnderecoPaciente | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EnderecoPacienteRoutingResolveService);
      service = TestBed.inject(EnderecoPacienteService);
      resultEnderecoPaciente = undefined;
    });

    describe('resolve', () => {
      it('should return IEnderecoPaciente returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEnderecoPaciente = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEnderecoPaciente).toEqual({ id: 123 });
      });

      it('should return new IEnderecoPaciente if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEnderecoPaciente = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEnderecoPaciente).toEqual(new EnderecoPaciente());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEnderecoPaciente = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEnderecoPaciente).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
