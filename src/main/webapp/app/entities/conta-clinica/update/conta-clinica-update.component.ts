import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IContaClinica, ContaClinica } from '../conta-clinica.model';
import { ContaClinicaService } from '../service/conta-clinica.service';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';

@Component({
  selector: 'jhi-conta-clinica-update',
  templateUrl: './conta-clinica-update.component.html',
})
export class ContaClinicaUpdateComponent implements OnInit {
  isSaving = false;

  cpfsCollection: IPaciente[] = [];

  editForm = this.fb.group({
    id: [],
    cpf: [null, [Validators.required]],
    senha: [],
    cpf: [],
  });

  constructor(
    protected contaClinicaService: ContaClinicaService,
    protected pacienteService: PacienteService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contaClinica }) => {
      this.updateForm(contaClinica);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contaClinica = this.createFromForm();
    if (contaClinica.id !== undefined) {
      this.subscribeToSaveResponse(this.contaClinicaService.update(contaClinica));
    } else {
      this.subscribeToSaveResponse(this.contaClinicaService.create(contaClinica));
    }
  }

  trackPacienteById(index: number, item: IPaciente): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContaClinica>>): void {
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

  protected updateForm(contaClinica: IContaClinica): void {
    this.editForm.patchValue({
      id: contaClinica.id,
      cpf: contaClinica.cpf,
      senha: contaClinica.senha,
      cpf: contaClinica.cpf,
    });

    this.cpfsCollection = this.pacienteService.addPacienteToCollectionIfMissing(this.cpfsCollection, contaClinica.cpf);
  }

  protected loadRelationshipsOptions(): void {
    this.pacienteService
      .query({ 'cpfId.specified': 'false' })
      .pipe(map((res: HttpResponse<IPaciente[]>) => res.body ?? []))
      .pipe(
        map((pacientes: IPaciente[]) => this.pacienteService.addPacienteToCollectionIfMissing(pacientes, this.editForm.get('cpf')!.value))
      )
      .subscribe((pacientes: IPaciente[]) => (this.cpfsCollection = pacientes));
  }

  protected createFromForm(): IContaClinica {
    return {
      ...new ContaClinica(),
      id: this.editForm.get(['id'])!.value,
      cpf: this.editForm.get(['cpf'])!.value,
      senha: this.editForm.get(['senha'])!.value,
      cpf: this.editForm.get(['cpf'])!.value,
    };
  }
}
