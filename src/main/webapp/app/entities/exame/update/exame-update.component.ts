import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IExame, Exame } from '../exame.model';
import { ExameService } from '../service/exame.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IMedico } from 'app/entities/medico/medico.model';
import { MedicoService } from 'app/entities/medico/service/medico.service';
import { IResultadoExame } from 'app/entities/resultado-exame/resultado-exame.model';
import { ResultadoExameService } from 'app/entities/resultado-exame/service/resultado-exame.service';
import { IContaClinica } from 'app/entities/conta-clinica/conta-clinica.model';
import { ContaClinicaService } from 'app/entities/conta-clinica/service/conta-clinica.service';

@Component({
  selector: 'jhi-exame-update',
  templateUrl: './exame-update.component.html',
})
export class ExameUpdateComponent implements OnInit {
  isSaving = false;

  nomemedicosCollection: IMedico[] = [];
  tipoexamesCollection: IResultadoExame[] = [];
  contaClinicasSharedCollection: IContaClinica[] = [];

  editForm = this.fb.group({
    id: [],
    tipoexame: [],
    valorexame: [],
    descontoconvenio: [],
    nomemmedico: [],
    prerequisito: [],
    datasolicitacao: [],
    dataresultado: [],
    nomemedico: [],
    tipoexame: [],
    contaClinica: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected exameService: ExameService,
    protected medicoService: MedicoService,
    protected resultadoExameService: ResultadoExameService,
    protected contaClinicaService: ContaClinicaService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ exame }) => {
      if (exame.id === undefined) {
        const today = dayjs().startOf('day');
        exame.datasolicitacao = today;
        exame.dataresultado = today;
      }

      this.updateForm(exame);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('clinicHealthSimpleApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const exame = this.createFromForm();
    if (exame.id !== undefined) {
      this.subscribeToSaveResponse(this.exameService.update(exame));
    } else {
      this.subscribeToSaveResponse(this.exameService.create(exame));
    }
  }

  trackMedicoById(index: number, item: IMedico): number {
    return item.id!;
  }

  trackResultadoExameById(index: number, item: IResultadoExame): number {
    return item.id!;
  }

  trackContaClinicaById(index: number, item: IContaClinica): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExame>>): void {
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

  protected updateForm(exame: IExame): void {
    this.editForm.patchValue({
      id: exame.id,
      tipoexame: exame.tipoexame,
      valorexame: exame.valorexame,
      descontoconvenio: exame.descontoconvenio,
      nomemmedico: exame.nomemmedico,
      prerequisito: exame.prerequisito,
      datasolicitacao: exame.datasolicitacao ? exame.datasolicitacao.format(DATE_TIME_FORMAT) : null,
      dataresultado: exame.dataresultado ? exame.dataresultado.format(DATE_TIME_FORMAT) : null,
      nomemedico: exame.nomemedico,
      tipoexame: exame.tipoexame,
      contaClinica: exame.contaClinica,
    });

    this.nomemedicosCollection = this.medicoService.addMedicoToCollectionIfMissing(this.nomemedicosCollection, exame.nomemedico);
    this.tipoexamesCollection = this.resultadoExameService.addResultadoExameToCollectionIfMissing(
      this.tipoexamesCollection,
      exame.tipoexame
    );
    this.contaClinicasSharedCollection = this.contaClinicaService.addContaClinicaToCollectionIfMissing(
      this.contaClinicasSharedCollection,
      exame.contaClinica
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medicoService
      .query({ 'nomeId.specified': 'false' })
      .pipe(map((res: HttpResponse<IMedico[]>) => res.body ?? []))
      .pipe(map((medicos: IMedico[]) => this.medicoService.addMedicoToCollectionIfMissing(medicos, this.editForm.get('nomemedico')!.value)))
      .subscribe((medicos: IMedico[]) => (this.nomemedicosCollection = medicos));

    this.resultadoExameService
      .query({ 'exameId.specified': 'false' })
      .pipe(map((res: HttpResponse<IResultadoExame[]>) => res.body ?? []))
      .pipe(
        map((resultadoExames: IResultadoExame[]) =>
          this.resultadoExameService.addResultadoExameToCollectionIfMissing(resultadoExames, this.editForm.get('tipoexame')!.value)
        )
      )
      .subscribe((resultadoExames: IResultadoExame[]) => (this.tipoexamesCollection = resultadoExames));

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

  protected createFromForm(): IExame {
    return {
      ...new Exame(),
      id: this.editForm.get(['id'])!.value,
      tipoexame: this.editForm.get(['tipoexame'])!.value,
      valorexame: this.editForm.get(['valorexame'])!.value,
      descontoconvenio: this.editForm.get(['descontoconvenio'])!.value,
      nomemmedico: this.editForm.get(['nomemmedico'])!.value,
      prerequisito: this.editForm.get(['prerequisito'])!.value,
      datasolicitacao: this.editForm.get(['datasolicitacao'])!.value
        ? dayjs(this.editForm.get(['datasolicitacao'])!.value, DATE_TIME_FORMAT)
        : undefined,
      dataresultado: this.editForm.get(['dataresultado'])!.value
        ? dayjs(this.editForm.get(['dataresultado'])!.value, DATE_TIME_FORMAT)
        : undefined,
      nomemedico: this.editForm.get(['nomemedico'])!.value,
      tipoexame: this.editForm.get(['tipoexame'])!.value,
      contaClinica: this.editForm.get(['contaClinica'])!.value,
    };
  }
}
