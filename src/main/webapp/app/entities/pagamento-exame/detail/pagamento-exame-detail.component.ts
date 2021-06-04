import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPagamentoExame } from '../pagamento-exame.model';

@Component({
  selector: 'jhi-pagamento-exame-detail',
  templateUrl: './pagamento-exame-detail.component.html',
})
export class PagamentoExameDetailComponent implements OnInit {
  pagamentoExame: IPagamentoExame | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pagamentoExame }) => {
      this.pagamentoExame = pagamentoExame;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
