jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContaClinica, ContaClinica } from '../conta-clinica.model';
import { ContaClinicaService } from '../service/conta-clinica.service';

import { ContaClinicaRoutingResolveService } from './conta-clinica-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContaClinica routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContaClinicaRoutingResolveService;
    let service: ContaClinicaService;
    let resultContaClinica: IContaClinica | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContaClinicaRoutingResolveService);
      service = TestBed.inject(ContaClinicaService);
      resultContaClinica = undefined;
    });

    describe('resolve', () => {
      it('should return IContaClinica returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContaClinica = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContaClinica).toEqual({ id: 123 });
      });

      it('should return new IContaClinica if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContaClinica = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContaClinica).toEqual(new ContaClinica());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContaClinica = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContaClinica).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
