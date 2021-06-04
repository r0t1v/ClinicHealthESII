jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PagamentoExameService } from '../service/pagamento-exame.service';
import { IPagamentoExame, PagamentoExame } from '../pagamento-exame.model';
import { IExame } from 'app/entities/exame/exame.model';
import { ExameService } from 'app/entities/exame/service/exame.service';

import { PagamentoExameUpdateComponent } from './pagamento-exame-update.component';

describe('Component Tests', () => {
  describe('PagamentoExame Management Update Component', () => {
    let comp: PagamentoExameUpdateComponent;
    let fixture: ComponentFixture<PagamentoExameUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let pagamentoExameService: PagamentoExameService;
    let exameService: ExameService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PagamentoExameUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PagamentoExameUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PagamentoExameUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      pagamentoExameService = TestBed.inject(PagamentoExameService);
      exameService = TestBed.inject(ExameService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Exame query and add missing value', () => {
        const pagamentoExame: IPagamentoExame = { id: 456 };
        const exame: IExame = { id: 16240 };
        pagamentoExame.exame = exame;

        const exameCollection: IExame[] = [{ id: 21318 }];
        spyOn(exameService, 'query').and.returnValue(of(new HttpResponse({ body: exameCollection })));
        const additionalExames = [exame];
        const expectedCollection: IExame[] = [...additionalExames, ...exameCollection];
        spyOn(exameService, 'addExameToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ pagamentoExame });
        comp.ngOnInit();

        expect(exameService.query).toHaveBeenCalled();
        expect(exameService.addExameToCollectionIfMissing).toHaveBeenCalledWith(exameCollection, ...additionalExames);
        expect(comp.examesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const pagamentoExame: IPagamentoExame = { id: 456 };
        const exame: IExame = { id: 10014 };
        pagamentoExame.exame = exame;

        activatedRoute.data = of({ pagamentoExame });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pagamentoExame));
        expect(comp.examesSharedCollection).toContain(exame);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pagamentoExame = { id: 123 };
        spyOn(pagamentoExameService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pagamentoExame });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pagamentoExame }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(pagamentoExameService.update).toHaveBeenCalledWith(pagamentoExame);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pagamentoExame = new PagamentoExame();
        spyOn(pagamentoExameService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pagamentoExame });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pagamentoExame }));
        saveSubject.complete();

        // THEN
        expect(pagamentoExameService.create).toHaveBeenCalledWith(pagamentoExame);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pagamentoExame = { id: 123 };
        spyOn(pagamentoExameService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pagamentoExame });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(pagamentoExameService.update).toHaveBeenCalledWith(pagamentoExame);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackExameById', () => {
        it('Should return tracked Exame primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackExameById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
