import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PagamentoExameComponent } from './list/pagamento-exame.component';
import { PagamentoExameDetailComponent } from './detail/pagamento-exame-detail.component';
import { PagamentoExameUpdateComponent } from './update/pagamento-exame-update.component';
import { PagamentoExameDeleteDialogComponent } from './delete/pagamento-exame-delete-dialog.component';
import { PagamentoExameRoutingModule } from './route/pagamento-exame-routing.module';

@NgModule({
  imports: [SharedModule, PagamentoExameRoutingModule],
  declarations: [
    PagamentoExameComponent,
    PagamentoExameDetailComponent,
    PagamentoExameUpdateComponent,
    PagamentoExameDeleteDialogComponent,
  ],
  entryComponents: [PagamentoExameDeleteDialogComponent],
})
export class PagamentoExameModule {}
