import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITipoConvenio, TipoConvenio } from '../tipo-convenio.model';
import { TipoConvenioService } from '../service/tipo-convenio.service';

@Injectable({ providedIn: 'root' })
export class TipoConvenioRoutingResolveService implements Resolve<ITipoConvenio> {
  constructor(protected service: TipoConvenioService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITipoConvenio> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((tipoConvenio: HttpResponse<TipoConvenio>) => {
          if (tipoConvenio.body) {
            return of(tipoConvenio.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TipoConvenio());
  }
}
