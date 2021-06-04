import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEnderecoPaciente, EnderecoPaciente } from '../endereco-paciente.model';
import { EnderecoPacienteService } from '../service/endereco-paciente.service';

@Injectable({ providedIn: 'root' })
export class EnderecoPacienteRoutingResolveService implements Resolve<IEnderecoPaciente> {
  constructor(protected service: EnderecoPacienteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEnderecoPaciente> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((enderecoPaciente: HttpResponse<EnderecoPaciente>) => {
          if (enderecoPaciente.body) {
            return of(enderecoPaciente.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new EnderecoPaciente());
  }
}
