import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { TipoConvenioComponent } from './list/tipo-convenio.component';
import { TipoConvenioDetailComponent } from './detail/tipo-convenio-detail.component';
import { TipoConvenioUpdateComponent } from './update/tipo-convenio-update.component';
import { TipoConvenioDeleteDialogComponent } from './delete/tipo-convenio-delete-dialog.component';
import { TipoConvenioRoutingModule } from './route/tipo-convenio-routing.module';

@NgModule({
  imports: [SharedModule, TipoConvenioRoutingModule],
  declarations: [TipoConvenioComponent, TipoConvenioDetailComponent, TipoConvenioUpdateComponent, TipoConvenioDeleteDialogComponent],
  entryComponents: [TipoConvenioDeleteDialogComponent],
})
export class TipoConvenioModule {}
