jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITipoConvenio, TipoConvenio } from '../tipo-convenio.model';
import { TipoConvenioService } from '../service/tipo-convenio.service';

import { TipoConvenioRoutingResolveService } from './tipo-convenio-routing-resolve.service';

describe('Service Tests', () => {
  describe('TipoConvenio routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TipoConvenioRoutingResolveService;
    let service: TipoConvenioService;
    let resultTipoConvenio: ITipoConvenio | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TipoConvenioRoutingResolveService);
      service = TestBed.inject(TipoConvenioService);
      resultTipoConvenio = undefined;
    });

    describe('resolve', () => {
      it('should return ITipoConvenio returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTipoConvenio = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTipoConvenio).toEqual({ id: 123 });
      });

      it('should return new ITipoConvenio if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTipoConvenio = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTipoConvenio).toEqual(new TipoConvenio());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTipoConvenio = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTipoConvenio).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
