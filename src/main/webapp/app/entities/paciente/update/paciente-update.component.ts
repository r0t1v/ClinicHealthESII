import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPaciente, Paciente } from '../paciente.model';
import { PacienteService } from '../service/paciente.service';
import { IContaClinica } from 'app/entities/conta-clinica/conta-clinica.model';
import { ContaClinicaService } from 'app/entities/conta-clinica/service/conta-clinica.service';

@Component({
  selector: 'jhi-paciente-update',
  templateUrl: './paciente-update.component.html',
})
export class PacienteUpdateComponent implements OnInit {
  isSaving = false;

  cpfsCollection: IContaClinica[] = [];

  editForm = this.fb.group({
    id: [],
    cpf: [],
    nome: [],
    idade: [],
    cpf: [],
  });

  constructor(
    protected pacienteService: PacienteService,
    protected contaClinicaService: ContaClinicaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ paciente }) => {
      this.updateForm(paciente);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const paciente = this.createFromForm();
    if (paciente.id !== undefined) {
      this.subscribeToSaveResponse(this.pacienteService.update(paciente));
    } else {
      this.subscribeToSaveResponse(this.pacienteService.create(paciente));
    }
  }

  trackContaClinicaById(index: number, item: IContaClinica): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPaciente>>): void {
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

  protected updateForm(paciente: IPaciente): void {
    this.editForm.patchValue({
      id: paciente.id,
      cpf: paciente.cpf,
      nome: paciente.nome,
      idade: paciente.idade,
      cpf: paciente.cpf,
    });

    this.cpfsCollection = this.contaClinicaService.addContaClinicaToCollectionIfMissing(this.cpfsCollection, paciente.cpf);
  }

  protected loadRelationshipsOptions(): void {
    this.contaClinicaService
      .query({ 'cpfId.specified': 'false' })
      .pipe(map((res: HttpResponse<IContaClinica[]>) => res.body ?? []))
      .pipe(
        map((contaClinicas: IContaClinica[]) =>
          this.contaClinicaService.addContaClinicaToCollectionIfMissing(contaClinicas, this.editForm.get('cpf')!.value)
        )
      )
      .subscribe((contaClinicas: IContaClinica[]) => (this.cpfsCollection = contaClinicas));
  }

  protected createFromForm(): IPaciente {
    return {
      ...new Paciente(),
      id: this.editForm.get(['id'])!.value,
      cpf: this.editForm.get(['cpf'])!.value,
      nome: this.editForm.get(['nome'])!.value,
      idade: this.editForm.get(['idade'])!.value,
      cpf: this.editForm.get(['cpf'])!.value,
    };
  }
}
