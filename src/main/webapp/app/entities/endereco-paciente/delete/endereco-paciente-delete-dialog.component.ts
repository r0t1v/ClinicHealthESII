import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEnderecoPaciente } from '../endereco-paciente.model';
import { EnderecoPacienteService } from '../service/endereco-paciente.service';

@Component({
  templateUrl: './endereco-paciente-delete-dialog.component.html',
})
export class EnderecoPacienteDeleteDialogComponent {
  enderecoPaciente?: IEnderecoPaciente;

  constructor(protected enderecoPacienteService: EnderecoPacienteService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.enderecoPacienteService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
