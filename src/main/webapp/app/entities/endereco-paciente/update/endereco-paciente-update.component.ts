import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEnderecoPaciente, EnderecoPaciente } from '../endereco-paciente.model';
import { EnderecoPacienteService } from '../service/endereco-paciente.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';

@Component({
  selector: 'jhi-endereco-paciente-update',
  templateUrl: './endereco-paciente-update.component.html',
})
export class EnderecoPacienteUpdateComponent implements OnInit {
  isSaving = false;

  pacientesSharedCollection: IPaciente[] = [];

  editForm = this.fb.group({
    id: [],
    cidade: [],
    logradouro: [],
    bairro: [],
    numero: [],
    referencia: [],
    cep: [],
    paciente: [],
  });

  constructor(
    protected enderecoPacienteService: EnderecoPacienteService,
    protected pacienteService: PacienteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enderecoPaciente }) => {
      this.updateForm(enderecoPaciente);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const enderecoPaciente = this.createFromForm();
    if (enderecoPaciente.id !== undefined) {
      this.subscribeToSaveResponse(this.enderecoPacienteService.update(enderecoPaciente));
    } else {
      this.subscribeToSaveResponse(this.enderecoPacienteService.create(enderecoPaciente));
    }
  }

  trackPacienteById(index: number, item: IPaciente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEnderecoPaciente>>): void {
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

  protected updateForm(enderecoPaciente: IEnderecoPaciente): void {
    this.editForm.patchValue({
      id: enderecoPaciente.id,
      cidade: enderecoPaciente.cidade,
      logradouro: enderecoPaciente.logradouro,
      bairro: enderecoPaciente.bairro,
      numero: enderecoPaciente.numero,
      referencia: enderecoPaciente.referencia,
      cep: enderecoPaciente.cep,
      paciente: enderecoPaciente.paciente,
    });

    this.pacientesSharedCollection = this.pacienteService.addPacienteToCollectionIfMissing(
      this.pacientesSharedCollection,
      enderecoPaciente.paciente
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

  protected createFromForm(): IEnderecoPaciente {
    return {
      ...new EnderecoPaciente(),
      id: this.editForm.get(['id'])!.value,
      cidade: this.editForm.get(['cidade'])!.value,
      logradouro: this.editForm.get(['logradouro'])!.value,
      bairro: this.editForm.get(['bairro'])!.value,
      numero: this.editForm.get(['numero'])!.value,
      referencia: this.editForm.get(['referencia'])!.value,
      cep: this.editForm.get(['cep'])!.value,
      paciente: this.editForm.get(['paciente'])!.value,
    };
  }
}
