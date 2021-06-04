import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ContatoPacienteComponent } from './list/contato-paciente.component';
import { ContatoPacienteDetailComponent } from './detail/contato-paciente-detail.component';
import { ContatoPacienteUpdateComponent } from './update/contato-paciente-update.component';
import { ContatoPacienteDeleteDialogComponent } from './delete/contato-paciente-delete-dialog.component';
import { ContatoPacienteRoutingModule } from './route/contato-paciente-routing.module';

@NgModule({
  imports: [SharedModule, ContatoPacienteRoutingModule],
  declarations: [
    ContatoPacienteComponent,
    ContatoPacienteDetailComponent,
    ContatoPacienteUpdateComponent,
    ContatoPacienteDeleteDialogComponent,
  ],
  entryComponents: [ContatoPacienteDeleteDialogComponent],
})
export class ContatoPacienteModule {}
