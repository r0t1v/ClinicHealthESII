import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PagamentoExameComponent } from '../list/pagamento-exame.component';
import { PagamentoExameDetailComponent } from '../detail/pagamento-exame-detail.component';
import { PagamentoExameUpdateComponent } from '../update/pagamento-exame-update.component';
import { PagamentoExameRoutingResolveService } from './pagamento-exame-routing-resolve.service';

const pagamentoExameRoute: Routes = [
  {
    path: '',
    component: PagamentoExameComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PagamentoExameDetailComponent,
    resolve: {
      pagamentoExame: PagamentoExameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PagamentoExameUpdateComponent,
    resolve: {
      pagamentoExame: PagamentoExameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PagamentoExameUpdateComponent,
    resolve: {
      pagamentoExame: PagamentoExameRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pagamentoExameRoute)],
  exports: [RouterModule],
})
export class PagamentoExameRoutingModule {}
