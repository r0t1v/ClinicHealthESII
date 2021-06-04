import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IResultadoExame, ResultadoExame } from '../resultado-exame.model';
import { ResultadoExameService } from '../service/resultado-exame.service';

@Component({
  selector: 'jhi-resultado-exame-update',
  templateUrl: './resultado-exame-update.component.html',
})
export class ResultadoExameUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    descricao: [],
    prescricao: [],
    indicacao: [],
    contraindicacao: [],
  });

  constructor(
    protected resultadoExameService: ResultadoExameService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resultadoExame }) => {
      this.updateForm(resultadoExame);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const resultadoExame = this.createFromForm();
    if (resultadoExame.id !== undefined) {
      this.subscribeToSaveResponse(this.resultadoExameService.update(resultadoExame));
    } else {
      this.subscribeToSaveResponse(this.resultadoExameService.create(resultadoExame));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IResultadoExame>>): void {
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

  protected updateForm(resultadoExame: IResultadoExame): void {
    this.editForm.patchValue({
      id: resultadoExame.id,
      descricao: resultadoExame.descricao,
      prescricao: resultadoExame.prescricao,
      indicacao: resultadoExame.indicacao,
      contraindicacao: resultadoExame.contraindicacao,
    });
  }

  protected createFromForm(): IResultadoExame {
    return {
      ...new ResultadoExame(),
      id: this.editForm.get(['id'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      prescricao: this.editForm.get(['prescricao'])!.value,
      indicacao: this.editForm.get(['indicacao'])!.value,
      contraindicacao: this.editForm.get(['contraindicacao'])!.value,
    };
  }
}
