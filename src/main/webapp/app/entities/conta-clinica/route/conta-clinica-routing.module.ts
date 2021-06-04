import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ContaClinicaComponent } from '../list/conta-clinica.component';
import { ContaClinicaDetailComponent } from '../detail/conta-clinica-detail.component';
import { ContaClinicaUpdateComponent } from '../update/conta-clinica-update.component';
import { ContaClinicaRoutingResolveService } from './conta-clinica-routing-resolve.service';

const contaClinicaRoute: Routes = [
  {
    path: '',
    component: ContaClinicaComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContaClinicaDetailComponent,
    resolve: {
      contaClinica: ContaClinicaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContaClinicaUpdateComponent,
    resolve: {
      contaClinica: ContaClinicaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContaClinicaUpdateComponent,
    resolve: {
      contaClinica: ContaClinicaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(contaClinicaRoute)],
  exports: [RouterModule],
})
export class ContaClinicaRoutingModule {}
