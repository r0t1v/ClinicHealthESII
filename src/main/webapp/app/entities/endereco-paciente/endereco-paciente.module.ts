import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { EnderecoPacienteComponent } from './list/endereco-paciente.component';
import { EnderecoPacienteDetailComponent } from './detail/endereco-paciente-detail.component';
import { EnderecoPacienteUpdateComponent } from './update/endereco-paciente-update.component';
import { EnderecoPacienteDeleteDialogComponent } from './delete/endereco-paciente-delete-dialog.component';
import { EnderecoPacienteRoutingModule } from './route/endereco-paciente-routing.module';

@NgModule({
  imports: [SharedModule, EnderecoPacienteRoutingModule],
  declarations: [
    EnderecoPacienteComponent,
    EnderecoPacienteDetailComponent,
    EnderecoPacienteUpdateComponent,
    EnderecoPacienteDeleteDialogComponent,
  ],
  entryComponents: [EnderecoPacienteDeleteDialogComponent],
})
export class EnderecoPacienteModule {}
