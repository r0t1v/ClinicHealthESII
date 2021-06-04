import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPagamentoExame, PagamentoExame } from '../pagamento-exame.model';
import { PagamentoExameService } from '../service/pagamento-exame.service';
import { IExame } from 'app/entities/exame/exame.model';
import { ExameService } from 'app/entities/exame/service/exame.service';

@Component({
  selector: 'jhi-pagamento-exame-update',
  templateUrl: './pagamento-exame-update.component.html',
})
export class PagamentoExameUpdateComponent implements OnInit {
  isSaving = false;

  examesSharedCollection: IExame[] = [];

  editForm = this.fb.group({
    id: [],
    formapagamento: [],
    conteudo: [],
    seliquidado: [],
    exame: [],
  });

  constructor(
    protected pagamentoExameService: PagamentoExameService,
    protected exameService: ExameService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pagamentoExame }) => {
      this.updateForm(pagamentoExame);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pagamentoExame = this.createFromForm();
    if (pagamentoExame.id !== undefined) {
      this.subscribeToSaveResponse(this.pagamentoExameService.update(pagamentoExame));
    } else {
      this.subscribeToSaveResponse(this.pagamentoExameService.create(pagamentoExame));
    }
  }

  trackExameById(index: number, item: IExame): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPagamentoExame>>): void {
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

  protected updateForm(pagamentoExame: IPagamentoExame): void {
    this.editForm.patchValue({
      id: pagamentoExame.id,
      formapagamento: pagamentoExame.formapagamento,
      conteudo: pagamentoExame.conteudo,
      seliquidado: pagamentoExame.seliquidado,
      exame: pagamentoExame.exame,
    });

    this.examesSharedCollection = this.exameService.addExameToCollectionIfMissing(this.examesSharedCollection, pagamentoExame.exame);
  }

  protected loadRelationshipsOptions(): void {
    this.exameService
      .query()
      .pipe(map((res: HttpResponse<IExame[]>) => res.body ?? []))
      .pipe(map((exames: IExame[]) => this.exameService.addExameToCollectionIfMissing(exames, this.editForm.get('exame')!.value)))
      .subscribe((exames: IExame[]) => (this.examesSharedCollection = exames));
  }

  protected createFromForm(): IPagamentoExame {
    return {
      ...new PagamentoExame(),
      id: this.editForm.get(['id'])!.value,
      formapagamento: this.editForm.get(['formapagamento'])!.value,
      conteudo: this.editForm.get(['conteudo'])!.value,
      seliquidado: this.editForm.get(['seliquidado'])!.value,
      exame: this.editForm.get(['exame'])!.value,
    };
  }
}
