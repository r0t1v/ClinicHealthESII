import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContatoPaciente } from '../contato-paciente.model';
import { ContatoPacienteService } from '../service/contato-paciente.service';

@Component({
  templateUrl: './contato-paciente-delete-dialog.component.html',
})
export class ContatoPacienteDeleteDialogComponent {
  contatoPaciente?: IContatoPaciente;

  constructor(protected contatoPacienteService: ContatoPacienteService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contatoPacienteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
