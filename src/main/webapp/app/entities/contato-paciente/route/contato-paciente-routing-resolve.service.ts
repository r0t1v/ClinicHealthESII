import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IContatoPaciente, ContatoPaciente } from '../contato-paciente.model';
import { ContatoPacienteService } from '../service/contato-paciente.service';

@Injectable({ providedIn: 'root' })
export class ContatoPacienteRoutingResolveService implements Resolve<IContatoPaciente> {
  constructor(protected service: ContatoPacienteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IContatoPaciente> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((contatoPaciente: HttpResponse<ContatoPaciente>) => {
          if (contatoPaciente.body) {
            return of(contatoPaciente.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ContatoPaciente());
  }
}
