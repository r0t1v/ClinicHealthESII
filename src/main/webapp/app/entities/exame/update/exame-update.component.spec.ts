jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExameService } from '../service/exame.service';
import { IExame, Exame } from '../exame.model';
import { IMedico } from 'app/entities/medico/medico.model';
import { MedicoService } from 'app/entities/medico/service/medico.service';
import { IResultadoExame } from 'app/entities/resultado-exame/resultado-exame.model';
import { ResultadoExameService } from 'app/entities/resultado-exame/service/resultado-exame.service';
import { IContaClinica } from 'app/entities/conta-clinica/conta-clinica.model';
import { ContaClinicaService } from 'app/entities/conta-clinica/service/conta-clinica.service';

import { ExameUpdateComponent } from './exame-update.component';

describe('Component Tests', () => {
  describe('Exame Management Update Component', () => {
    let comp: ExameUpdateComponent;
    let fixture: ComponentFixture<ExameUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let exameService: ExameService;
    let medicoService: MedicoService;
    let resultadoExameService: ResultadoExameService;
    let contaClinicaService: ContaClinicaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExameUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ExameUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExameUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      exameService = TestBed.inject(ExameService);
      medicoService = TestBed.inject(MedicoService);
      resultadoExameService = TestBed.inject(ResultadoExameService);
      contaClinicaService = TestBed.inject(ContaClinicaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call nomemedico query and add missing value', () => {
        const exame: IExame = { id: 456 };
        const nomemedico: IMedico = { id: 60324 };
        exame.nomemedico = nomemedico;

        const nomemedicoCollection: IMedico[] = [{ id: 37741 }];
        spyOn(medicoService, 'query').and.returnValue(of(new HttpResponse({ body: nomemedicoCollection })));
        const expectedCollection: IMedico[] = [nomemedico, ...nomemedicoCollection];
        spyOn(medicoService, 'addMedicoToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ exame });
        comp.ngOnInit();

        expect(medicoService.query).toHaveBeenCalled();
        expect(medicoService.addMedicoToCollectionIfMissing).toHaveBeenCalledWith(nomemedicoCollection, nomemedico);
        expect(comp.nomemedicosCollection).toEqual(expectedCollection);
      });

      it('Should call tipoexame query and add missing value', () => {
        const exame: IExame = { id: 456 };
        const tipoexame: IResultadoExame = { id: 41570 };
        exame.tipoexame = tipoexame;

        const tipoexameCollection: IResultadoExame[] = [{ id: 89249 }];
        spyOn(resultadoExameService, 'query').and.returnValue(of(new HttpResponse({ body: tipoexameCollection })));
        const expectedCollection: IResultadoExame[] = [tipoexame, ...tipoexameCollection];
        spyOn(resultadoExameService, 'addResultadoExameToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ exame });
        comp.ngOnInit();

        expect(resultadoExameService.query).toHaveBeenCalled();
        expect(resultadoExameService.addResultadoExameToCollectionIfMissing).toHaveBeenCalledWith(tipoexameCollection, tipoexame);
        expect(comp.tipoexamesCollection).toEqual(expectedCollection);
      });

      it('Should call ContaClinica query and add missing value', () => {
        const exame: IExame = { id: 456 };
        const contaClinica: IContaClinica = { id: 15715 };
        exame.contaClinica = contaClinica;

        const contaClinicaCollection: IContaClinica[] = [{ id: 44434 }];
        spyOn(contaClinicaService, 'query').and.returnValue(of(new HttpResponse({ body: contaClinicaCollection })));
        const additionalContaClinicas = [contaClinica];
        const expectedCollection: IContaClinica[] = [...additionalContaClinicas, ...contaClinicaCollection];
        spyOn(contaClinicaService, 'addContaClinicaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ exame });
        comp.ngOnInit();

        expect(contaClinicaService.query).toHaveBeenCalled();
        expect(contaClinicaService.addContaClinicaToCollectionIfMissing).toHaveBeenCalledWith(
          contaClinicaCollection,
          ...additionalContaClinicas
        );
        expect(comp.contaClinicasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const exame: IExame = { id: 456 };
        const nomemedico: IMedico = { id: 41213 };
        exame.nomemedico = nomemedico;
        const tipoexame: IResultadoExame = { id: 33826 };
        exame.tipoexame = tipoexame;
        const contaClinica: IContaClinica = { id: 55077 };
        exame.contaClinica = contaClinica;

        activatedRoute.data = of({ exame });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(exame));
        expect(comp.nomemedicosCollection).toContain(nomemedico);
        expect(comp.tipoexamesCollection).toContain(tipoexame);
        expect(comp.contaClinicasSharedCollection).toContain(contaClinica);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const exame = { id: 123 };
        spyOn(exameService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ exame });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: exame }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(exameService.update).toHaveBeenCalledWith(exame);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const exame = new Exame();
        spyOn(exameService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ exame });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: exame }));
        saveSubject.complete();

        // THEN
        expect(exameService.create).toHaveBeenCalledWith(exame);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const exame = { id: 123 };
        spyOn(exameService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ exame });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(exameService.update).toHaveBeenCalledWith(exame);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackMedicoById', () => {
        it('Should return tracked Medico primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackMedicoById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackResultadoExameById', () => {
        it('Should return tracked ResultadoExame primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackResultadoExameById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackContaClinicaById', () => {
        it('Should return tracked ContaClinica primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContaClinicaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
