import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITipoConvenio } from '../tipo-convenio.model';

@Component({
  selector: 'jhi-tipo-convenio-detail',
  templateUrl: './tipo-convenio-detail.component.html',
})
export class TipoConvenioDetailComponent implements OnInit {
  tipoConvenio: ITipoConvenio | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoConvenio }) => {
      this.tipoConvenio = tipoConvenio;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
