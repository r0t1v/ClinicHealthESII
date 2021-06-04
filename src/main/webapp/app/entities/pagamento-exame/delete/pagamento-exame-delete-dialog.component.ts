import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPagamentoExame } from '../pagamento-exame.model';
import { PagamentoExameService } from '../service/pagamento-exame.service';

@Component({
  templateUrl: './pagamento-exame-delete-dialog.component.html',
})
export class PagamentoExameDeleteDialogComponent {
  pagamentoExame?: IPagamentoExame;

  constructor(protected pagamentoExameService: PagamentoExameService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pagamentoExameService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
