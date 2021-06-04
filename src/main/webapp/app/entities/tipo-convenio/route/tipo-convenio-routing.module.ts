import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TipoConvenioComponent } from '../list/tipo-convenio.component';
import { TipoConvenioDetailComponent } from '../detail/tipo-convenio-detail.component';
import { TipoConvenioUpdateComponent } from '../update/tipo-convenio-update.component';
import { TipoConvenioRoutingResolveService } from './tipo-convenio-routing-resolve.service';

const tipoConvenioRoute: Routes = [
  {
    path: '',
    component: TipoConvenioComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoConvenioDetailComponent,
    resolve: {
      tipoConvenio: TipoConvenioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoConvenioUpdateComponent,
    resolve: {
      tipoConvenio: TipoConvenioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoConvenioUpdateComponent,
    resolve: {
      tipoConvenio: TipoConvenioRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(tipoConvenioRoute)],
  exports: [RouterModule],
})
export class TipoConvenioRoutingModule {}
