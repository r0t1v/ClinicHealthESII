import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IContaClinica } from '../conta-clinica.model';
import { ContaClinicaService } from '../service/conta-clinica.service';

@Component({
  templateUrl: './conta-clinica-delete-dialog.component.html',
})
export class ContaClinicaDeleteDialogComponent {
  contaClinica?: IContaClinica;

  constructor(protected contaClinicaService: ContaClinicaService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.contaClinicaService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
