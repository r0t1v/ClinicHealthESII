import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ResultadoExameComponent } from '../list/resultado-exame.component';
import { ResultadoExameDetailComponent } from '../detail/resultado-exame-detail.component';
import { ResultadoExameUpdateComponent } from '../update/resultado-exame-update.component';
import { ResultadoExameRoutingResolveService } from './resultado-exame-routing-resolve.service';

const resultadoExameRoute: Routes = [
  {
    path: '',
    component: ResultadoExameComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ResultadoExameDetailComponent,
    resolve: {
      resultadoExame: ResultadoExameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ResultadoExameUpdateComponent,
    resolve: {
      resultadoExame: ResultadoExameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ResultadoExameUpdateComponent,
    resolve: {
      resultadoExame: ResultadoExameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(resultadoExameRoute)],
  exports: [RouterModule],
})
export class ResultadoExameRoutingModule {}
