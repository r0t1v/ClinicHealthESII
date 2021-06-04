import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEnderecoPaciente } from '../endereco-paciente.model';

@Component({
  selector: 'jhi-endereco-paciente-detail',
  templateUrl: './endereco-paciente-detail.component.html',
})
export class EnderecoPacienteDetailComponent implements OnInit {
  enderecoPaciente: IEnderecoPaciente | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enderecoPaciente }) => {
      this.enderecoPaciente = enderecoPaciente;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
