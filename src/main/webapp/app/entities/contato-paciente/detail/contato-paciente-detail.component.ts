import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContatoPaciente } from '../contato-paciente.model';

@Component({
  selector: 'jhi-contato-paciente-detail',
  templateUrl: './contato-paciente-detail.component.html',
})
export class ContatoPacienteDetailComponent implements OnInit {
  contatoPaciente: IContatoPaciente | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contatoPaciente }) => {
      this.contatoPaciente = contatoPaciente;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
