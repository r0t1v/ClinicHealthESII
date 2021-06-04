jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IResultadoExame, ResultadoExame } from '../resultado-exame.model';
import { ResultadoExameService } from '../service/resultado-exame.service';

import { ResultadoExameRoutingResolveService } from './resultado-exame-routing-resolve.service';

describe('Service Tests', () => {
  describe('ResultadoExame routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ResultadoExameRoutingResolveService;
    let service: ResultadoExameService;
    let resultResultadoExame: IResultadoExame | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ResultadoExameRoutingResolveService);
      service = TestBed.inject(ResultadoExameService);
      resultResultadoExame = undefined;
    });

    describe('resolve', () => {
      it('should return IResultadoExame returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultResultadoExame = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultResultadoExame).toEqual({ id: 123 });
      });

      it('should return new IResultadoExame if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultResultadoExame = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultResultadoExame).toEqual(new ResultadoExame());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultResultadoExame = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultResultadoExame).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
