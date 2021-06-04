import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITipoConvenio, TipoConvenio } from '../tipo-convenio.model';
import { TipoConvenioService } from '../service/tipo-convenio.service';
import { IContaClinica } from 'app/entities/conta-clinica/conta-clinica.model';
import { ContaClinicaService } from 'app/entities/conta-clinica/service/conta-clinica.service';

@Component({
  selector: 'jhi-tipo-convenio-update',
  templateUrl: './tipo-convenio-update.component.html',
})
export class TipoConvenioUpdateComponent implements OnInit {
  isSaving = false;

  contaClinicasSharedCollection: IContaClinica[] = [];

  editForm = this.fb.group({
    id: [],
    convenio: [],
    codigoconvenio: [],
    nomeconvenio: [],
    contato: [],
    contaClinica: [],
  });

  constructor(
    protected tipoConvenioService: TipoConvenioService,
    protected contaClinicaService: ContaClinicaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ tipoConvenio }) => {
      this.updateForm(tipoConvenio);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const tipoConvenio = this.createFromForm();
    if (tipoConvenio.id !== undefined) {
      this.subscribeToSaveResponse(this.tipoConvenioService.update(tipoConvenio));
    } else {
      this.subscribeToSaveResponse(this.tipoConvenioService.create(tipoConvenio));
    }
  }

  trackContaClinicaById(index: number, item: IContaClinica): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITipoConvenio>>): void {
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

  protected updateForm(tipoConvenio: ITipoConvenio): void {
    this.editForm.patchValue({
      id: tipoConvenio.id,
      convenio: tipoConvenio.convenio,
      codigoconvenio: tipoConvenio.codigoconvenio,
      nomeconvenio: tipoConvenio.nomeconvenio,
      contato: tipoConvenio.contato,
      contaClinica: tipoConvenio.contaClinica,
    });

    this.contaClinicasSharedCollection = this.contaClinicaService.addContaClinicaToCollectionIfMissing(
      this.contaClinicasSharedCollection,
      tipoConvenio.contaClinica
    );
  }

  protected loadRelationshipsOptions(): void {
    this.contaClinicaService
      .query()
      .pipe(map((res: HttpResponse<IContaClinica[]>) => res.body ?? []))
      .pipe(
        map((contaClinicas: IContaClinica[]) =>
          this.contaClinicaService.addContaClinicaToCollectionIfMissing(contaClinicas, this.editForm.get('contaClinica')!.value)
        )
      )
      .subscribe((contaClinicas: IContaClinica[]) => (this.contaClinicasSharedCollection = contaClinicas));
  }

  protected createFromForm(): ITipoConvenio {
    return {
      ...new TipoConvenio(),
      id: this.editForm.get(['id'])!.value,
      convenio: this.editForm.get(['convenio'])!.value,
      codigoconvenio: this.editForm.get(['codigoconvenio'])!.value,
      nomeconvenio: this.editForm.get(['nomeconvenio'])!.value,
      contato: this.editForm.get(['contato'])!.value,
      contaClinica: this.editForm.get(['contaClinica'])!.value,
    };
  }
}
