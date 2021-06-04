import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ContaClinicaComponent } from './list/conta-clinica.component';
import { ContaClinicaDetailComponent } from './detail/conta-clinica-detail.component';
import { ContaClinicaUpdateComponent } from './update/conta-clinica-update.component';
import { ContaClinicaDeleteDialogComponent } from './delete/conta-clinica-delete-dialog.component';
import { ContaClinicaRoutingModule } from './route/conta-clinica-routing.module';

@NgModule({
  imports: [SharedModule, ContaClinicaRoutingModule],
  declarations: [ContaClinicaComponent, ContaClinicaDetailComponent, ContaClinicaUpdateComponent, ContaClinicaDeleteDialogComponent],
  entryComponents: [ContaClinicaDeleteDialogComponent],
})
export class ContaClinicaModule {}
