jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContaClinicaService } from '../service/conta-clinica.service';
import { IContaClinica, ContaClinica } from '../conta-clinica.model';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';

import { ContaClinicaUpdateComponent } from './conta-clinica-update.component';

describe('Component Tests', () => {
  describe('ContaClinica Management Update Component', () => {
    let comp: ContaClinicaUpdateComponent;
    let fixture: ComponentFixture<ContaClinicaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contaClinicaService: ContaClinicaService;
    let pacienteService: PacienteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContaClinicaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContaClinicaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContaClinicaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contaClinicaService = TestBed.inject(ContaClinicaService);
      pacienteService = TestBed.inject(PacienteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call cpf query and add missing value', () => {
        const contaClinica: IContaClinica = { id: 456 };
        const cpf: IPaciente = { id: 11606 };
        contaClinica.cpf = cpf;

        const cpfCollection: IPaciente[] = [{ id: 30458 }];
        spyOn(pacienteService, 'query').and.returnValue(of(new HttpResponse({ body: cpfCollection })));
        const expectedCollection: IPaciente[] = [cpf, ...cpfCollection];
        spyOn(pacienteService, 'addPacienteToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ contaClinica });
        comp.ngOnInit();

        expect(pacienteService.query).toHaveBeenCalled();
        expect(pacienteService.addPacienteToCollectionIfMissing).toHaveBeenCalledWith(cpfCollection, cpf);
        expect(comp.cpfsCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const contaClinica: IContaClinica = { id: 456 };
        const cpf: IPaciente = { id: 75022 };
        contaClinica.cpf = cpf;

        activatedRoute.data = of({ contaClinica });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contaClinica));
        expect(comp.cpfsCollection).toContain(cpf);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const contaClinica = { id: 123 };
        spyOn(contaClinicaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ contaClinica });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contaClinica }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(contaClinicaService.update).toHaveBeenCalledWith(contaClinica);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const contaClinica = new ContaClinica();
        spyOn(contaClinicaService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ contaClinica });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contaClinica }));
        saveSubject.complete();

        // THEN
        expect(contaClinicaService.create).toHaveBeenCalledWith(contaClinica);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const contaClinica = { id: 123 };
        spyOn(contaClinicaService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ contaClinica });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(contaClinicaService.update).toHaveBeenCalledWith(contaClinica);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPacienteById', () => {
        it('Should return tracked Paciente primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPacienteById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
