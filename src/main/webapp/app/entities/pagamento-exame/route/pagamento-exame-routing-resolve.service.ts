import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPagamentoExame, PagamentoExame } from '../pagamento-exame.model';
import { PagamentoExameService } from '../service/pagamento-exame.service';

@Injectable({ providedIn: 'root' })
export class PagamentoExameRoutingResolveService implements Resolve<IPagamentoExame> {
  constructor(protected service: PagamentoExameService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPagamentoExame> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pagamentoExame: HttpResponse<PagamentoExame>) => {
          if (pagamentoExame.body) {
            return of(pagamentoExame.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PagamentoExame());
  }
}
