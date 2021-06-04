jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TipoConvenioService } from '../service/tipo-convenio.service';
import { ITipoConvenio, TipoConvenio } from '../tipo-convenio.model';
import { IContaClinica } from 'app/entities/conta-clinica/conta-clinica.model';
import { ContaClinicaService } from 'app/entities/conta-clinica/service/conta-clinica.service';

import { TipoConvenioUpdateComponent } from './tipo-convenio-update.component';

describe('Component Tests', () => {
  describe('TipoConvenio Management Update Component', () => {
    let comp: TipoConvenioUpdateComponent;
    let fixture: ComponentFixture<TipoConvenioUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tipoConvenioService: TipoConvenioService;
    let contaClinicaService: ContaClinicaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TipoConvenioUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TipoConvenioUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TipoConvenioUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tipoConvenioService = TestBed.inject(TipoConvenioService);
      contaClinicaService = TestBed.inject(ContaClinicaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ContaClinica query and add missing value', () => {
        const tipoConvenio: ITipoConvenio = { id: 456 };
        const contaClinica: IContaClinica = { id: 305 };
        tipoConvenio.contaClinica = contaClinica;

        const contaClinicaCollection: IContaClinica[] = [{ id: 51864 }];
        spyOn(contaClinicaService, 'query').and.returnValue(of(new HttpResponse({ body: contaClinicaCollection })));
        const additionalContaClinicas = [contaClinica];
        const expectedCollection: IContaClinica[] = [...additionalContaClinicas, ...contaClinicaCollection];
        spyOn(contaClinicaService, 'addContaClinicaToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ tipoConvenio });
        comp.ngOnInit();

        expect(contaClinicaService.query).toHaveBeenCalled();
        expect(contaClinicaService.addContaClinicaToCollectionIfMissing).toHaveBeenCalledWith(
          contaClinicaCollection,
          ...additionalContaClinicas
        );
        expect(comp.contaClinicasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const tipoConvenio: ITipoConvenio = { id: 456 };
        const contaClinica: IContaClinica = { id: 98547 };
        tipoConvenio.contaClinica = contaClinica;

        activatedRoute.data = of({ tipoConvenio });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tipoConvenio));
        expect(comp.contaClinicasSharedCollection).toContain(contaClinica);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tipoConvenio = { id: 123 };
        spyOn(tipoConvenioService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tipoConvenio });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tipoConvenio }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tipoConvenioService.update).toHaveBeenCalledWith(tipoConvenio);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tipoConvenio = new TipoConvenio();
        spyOn(tipoConvenioService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tipoConvenio });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tipoConvenio }));
        saveSubject.complete();

        // THEN
        expect(tipoConvenioService.create).toHaveBeenCalledWith(tipoConvenio);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const tipoConvenio = { id: 123 };
        spyOn(tipoConvenioService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ tipoConvenio });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tipoConvenioService.update).toHaveBeenCalledWith(tipoConvenio);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
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
