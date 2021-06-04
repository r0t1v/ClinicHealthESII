import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContatoPacienteComponent } from '../list/contato-paciente.component';
import { ContatoPacienteDetailComponent } from '../detail/contato-paciente-detail.component';
import { ContatoPacienteUpdateComponent } from '../update/contato-paciente-update.component';
import { ContatoPacienteRoutingResolveService } from './contato-paciente-routing-resolve.service';

const contatoPacienteRoute: Routes = [
  {
    path: '',
    component: ContatoPacienteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContatoPacienteDetailComponent,
    resolve: {
      contatoPaciente: ContatoPacienteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContatoPacienteUpdateComponent,
    resolve: {
      contatoPaciente: ContatoPacienteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContatoPacienteUpdateComponent,
    resolve: {
      contatoPaciente: ContatoPacienteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contatoPacienteRoute)],
  exports: [RouterModule],
})
export class ContatoPacienteRoutingModule {}
