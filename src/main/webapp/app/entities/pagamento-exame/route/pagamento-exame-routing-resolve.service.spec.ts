jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPagamentoExame, PagamentoExame } from '../pagamento-exame.model';
import { PagamentoExameService } from '../service/pagamento-exame.service';

import { PagamentoExameRoutingResolveService } from './pagamento-exame-routing-resolve.service';

describe('Service Tests', () => {
  describe('PagamentoExame routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PagamentoExameRoutingResolveService;
    let service: PagamentoExameService;
    let resultPagamentoExame: IPagamentoExame | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PagamentoExameRoutingResolveService);
      service = TestBed.inject(PagamentoExameService);
      resultPagamentoExame = undefined;
    });

    describe('resolve', () => {
      it('should return IPagamentoExame returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPagamentoExame = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPagamentoExame).toEqual({ id: 123 });
      });

      it('should return new IPagamentoExame if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPagamentoExame = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPagamentoExame).toEqual(new PagamentoExame());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPagamentoExame = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPagamentoExame).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
