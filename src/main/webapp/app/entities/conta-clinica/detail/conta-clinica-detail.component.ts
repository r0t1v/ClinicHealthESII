import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContaClinica } from '../conta-clinica.model';

@Component({
  selector: 'jhi-conta-clinica-detail',
  templateUrl: './conta-clinica-detail.component.html',
})
export class ContaClinicaDetailComponent implements OnInit {
  contaClinica: IContaClinica | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contaClinica }) => {
      this.contaClinica = contaClinica;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
