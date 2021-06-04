import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IContatoPaciente, ContatoPaciente } from '../contato-paciente.model';
import { ContatoPacienteService } from '../service/contato-paciente.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';

@Component({
  selector: 'jhi-contato-paciente-update',
  templateUrl: './contato-paciente-update.component.html',
})
export class ContatoPacienteUpdateComponent implements OnInit {
  isSaving = false;

  pacientesSharedCollection: IPaciente[] = [];

  editForm = this.fb.group({
    id: [],
    tipo: [],
    conteudo: [],
    paciente: [],
  });

  constructor(
    protected contatoPacienteService: ContatoPacienteService,
    protected pacienteService: PacienteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contatoPaciente }) => {
      this.updateForm(contatoPaciente);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contatoPaciente = this.createFromForm();
    if (contatoPaciente.id !== undefined) {
      this.subscribeToSaveResponse(this.contatoPacienteService.update(contatoPaciente));
    } else {
      this.subscribeToSaveResponse(this.contatoPacienteService.create(contatoPaciente));
    }
  }

  trackPacienteById(index: number, item: IPaciente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContatoPaciente>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(contatoPaciente: IContatoPaciente): void {
    this.editForm.patchValue({
      id: contatoPaciente.id,
      tipo: contatoPaciente.tipo,
      conteudo: contatoPaciente.conteudo,
      paciente: contatoPaciente.paciente,
    });

    this.pacientesSharedCollection = this.pacienteService.addPacienteToCollectionIfMissing(
      this.pacientesSharedCollection,
      contatoPaciente.paciente
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pacienteService
      .query()
      .pipe(map((res: HttpResponse<IPaciente[]>) => res.body ?? []))
      .pipe(
        map((pacientes: IPaciente[]) =>
          this.pacienteService.addPacienteToCollectionIfMissing(pacientes, this.editForm.get('paciente')!.value)
        )
      )
      .subscribe((pacientes: IPaciente[]) => (this.pacientesSharedCollection = pacientes));
  }

  protected createFromForm(): IContatoPaciente {
    return {
      ...new ContatoPaciente(),
      id: this.editForm.get(['id'])!.value,
      tipo: this.editForm.get(['tipo'])!.value,
      conteudo: this.editForm.get(['conteudo'])!.value,
      paciente: this.editForm.get(['paciente'])!.value,
    };
  }
}
