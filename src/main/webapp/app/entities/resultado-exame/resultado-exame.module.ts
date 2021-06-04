import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ResultadoExameComponent } from './list/resultado-exame.component';
import { ResultadoExameDetailComponent } from './detail/resultado-exame-detail.component';
import { ResultadoExameUpdateComponent } from './update/resultado-exame-update.component';
import { ResultadoExameDeleteDialogComponent } from './delete/resultado-exame-delete-dialog.component';
import { ResultadoExameRoutingModule } from './route/resultado-exame-routing.module';

@NgModule({
  imports: [SharedModule, ResultadoExameRoutingModule],
  declarations: [
    ResultadoExameComponent,
    ResultadoExameDetailComponent,
    ResultadoExameUpdateComponent,
    ResultadoExameDeleteDialogComponent,
  ],
  entryComponents: [ResultadoExameDeleteDialogComponent],
})
export class ResultadoExameModule {}
