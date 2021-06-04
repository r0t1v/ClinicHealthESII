jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EnderecoPacienteService } from '../service/endereco-paciente.service';
import { IEnderecoPaciente, EnderecoPaciente } from '../endereco-paciente.model';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';

import { EnderecoPacienteUpdateComponent } from './endereco-paciente-update.component';

describe('Component Tests', () => {
  describe('EnderecoPaciente Management Update Component', () => {
    let comp: EnderecoPacienteUpdateComponent;
    let fixture: ComponentFixture<EnderecoPacienteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let enderecoPacienteService: EnderecoPacienteService;
    let pacienteService: PacienteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EnderecoPacienteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EnderecoPacienteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EnderecoPacienteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      enderecoPacienteService = TestBed.inject(EnderecoPacienteService);
      pacienteService = TestBed.inject(PacienteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Paciente query and add missing value', () => {
        const enderecoPaciente: IEnderecoPaciente = { id: 456 };
        const paciente: IPaciente = { id: 11496 };
        enderecoPaciente.paciente = paciente;

        const pacienteCollection: IPaciente[] = [{ id: 43734 }];
        spyOn(pacienteService, 'query').and.returnValue(of(new HttpResponse({ body: pacienteCollection })));
        const additionalPacientes = [paciente];
        const expectedCollection: IPaciente[] = [...additionalPacientes, ...pacienteCollection];
        spyOn(pacienteService, 'addPacienteToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ enderecoPaciente });
        comp.ngOnInit();

        expect(pacienteService.query).toHaveBeenCalled();
        expect(pacienteService.addPacienteToCollectionIfMissing).toHaveBeenCalledWith(pacienteCollection, ...additionalPacientes);
        expect(comp.pacientesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const enderecoPaciente: IEnderecoPaciente = { id: 456 };
        const paciente: IPaciente = { id: 58544 };
        enderecoPaciente.paciente = paciente;

        activatedRoute.data = of({ enderecoPaciente });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(enderecoPaciente));
        expect(comp.pacientesSharedCollection).toContain(paciente);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const enderecoPaciente = { id: 123 };
        spyOn(enderecoPacienteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ enderecoPaciente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: enderecoPaciente }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(enderecoPacienteService.update).toHaveBeenCalledWith(enderecoPaciente);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const enderecoPaciente = new EnderecoPaciente();
        spyOn(enderecoPacienteService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ enderecoPaciente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: enderecoPaciente }));
        saveSubject.complete();

        // THEN
        expect(enderecoPacienteService.create).toHaveBeenCalledWith(enderecoPaciente);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const enderecoPaciente = { id: 123 };
        spyOn(enderecoPacienteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ enderecoPaciente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(enderecoPacienteService.update).toHaveBeenCalledWith(enderecoPaciente);
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
