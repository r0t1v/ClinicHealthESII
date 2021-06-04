import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EnderecoPacienteComponent } from '../list/endereco-paciente.component';
import { EnderecoPacienteDetailComponent } from '../detail/endereco-paciente-detail.component';
import { EnderecoPacienteUpdateComponent } from '../update/endereco-paciente-update.component';
import { EnderecoPacienteRoutingResolveService } from './endereco-paciente-routing-resolve.service';

const enderecoPacienteRoute: Routes = [
  {
    path: '',
    component: EnderecoPacienteComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EnderecoPacienteDetailComponent,
    resolve: {
      enderecoPaciente: EnderecoPacienteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EnderecoPacienteUpdateComponent,
    resolve: {
      enderecoPaciente: EnderecoPacienteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EnderecoPacienteUpdateComponent,
    resolve: {
      enderecoPaciente: EnderecoPacienteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(enderecoPacienteRoute)],
  exports: [RouterModule],
})
export class EnderecoPacienteRoutingModule {}
