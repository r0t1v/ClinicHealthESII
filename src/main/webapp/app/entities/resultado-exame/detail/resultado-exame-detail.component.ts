import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResultadoExame } from '../resultado-exame.model';

@Component({
  selector: 'jhi-resultado-exame-detail',
  templateUrl: './resultado-exame-detail.component.html',
})
export class ResultadoExameDetailComponent implements OnInit {
  resultadoExame: IResultadoExame | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resultadoExame }) => {
      this.resultadoExame = resultadoExame;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
