import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResultadoExame, ResultadoExame } from '../resultado-exame.model';
import { ResultadoExameService } from '../service/resultado-exame.service';

@Injectable({ providedIn: 'root' })
export class ResultadoExameRoutingResolveService implements Resolve<IResultadoExame> {
  constructor(protected service: ResultadoExameService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IResultadoExame> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((resultadoExame: HttpResponse<ResultadoExame>) => {
          if (resultadoExame.body) {
            return of(resultadoExame.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ResultadoExame());
  }
}
