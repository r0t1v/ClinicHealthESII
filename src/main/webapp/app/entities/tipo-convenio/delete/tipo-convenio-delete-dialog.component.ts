import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITipoConvenio } from '../tipo-convenio.model';
import { TipoConvenioService } from '../service/tipo-convenio.service';

@Component({
  templateUrl: './tipo-convenio-delete-dialog.component.html',
})
export class TipoConvenioDeleteDialogComponent {
  tipoConvenio?: ITipoConvenio;

  constructor(protected tipoConvenioService: TipoConvenioService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tipoConvenioService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
