import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContaClinica, ContaClinica } from '../conta-clinica.model';
import { ContaClinicaService } from '../service/conta-clinica.service';

@Injectable({ providedIn: 'root' })
export class ContaClinicaRoutingResolveService implements Resolve<IContaClinica> {
  constructor(protected service: ContaClinicaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContaClinica> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contaClinica: HttpResponse<ContaClinica>) => {
          if (contaClinica.body) {
            return of(contaClinica.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContaClinica());
  }
}
