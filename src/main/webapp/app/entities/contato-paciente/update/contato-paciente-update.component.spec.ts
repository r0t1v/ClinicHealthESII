jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ContatoPacienteService } from '../service/contato-paciente.service';
import { IContatoPaciente, ContatoPaciente } from '../contato-paciente.model';
import { IPaciente } from 'app/entities/paciente/paciente.model';
import { PacienteService } from 'app/entities/paciente/service/paciente.service';

import { ContatoPacienteUpdateComponent } from './contato-paciente-update.component';

describe('Component Tests', () => {
  describe('ContatoPaciente Management Update Component', () => {
    let comp: ContatoPacienteUpdateComponent;
    let fixture: ComponentFixture<ContatoPacienteUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let contatoPacienteService: ContatoPacienteService;
    let pacienteService: PacienteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ContatoPacienteUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ContatoPacienteUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContatoPacienteUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      contatoPacienteService = TestBed.inject(ContatoPacienteService);
      pacienteService = TestBed.inject(PacienteService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Paciente query and add missing value', () => {
        const contatoPaciente: IContatoPaciente = { id: 456 };
        const paciente: IPaciente = { id: 70153 };
        contatoPaciente.paciente = paciente;

        const pacienteCollection: IPaciente[] = [{ id: 1052 }];
        spyOn(pacienteService, 'query').and.returnValue(of(new HttpResponse({ body: pacienteCollection })));
        const additionalPacientes = [paciente];
        const expectedCollection: IPaciente[] = [...additionalPacientes, ...pacienteCollection];
        spyOn(pacienteService, 'addPacienteToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ contatoPaciente });
        comp.ngOnInit();

        expect(pacienteService.query).toHaveBeenCalled();
        expect(pacienteService.addPacienteToCollectionIfMissing).toHaveBeenCalledWith(pacienteCollection, ...additionalPacientes);
        expect(comp.pacientesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const contatoPaciente: IContatoPaciente = { id: 456 };
        const paciente: IPaciente = { id: 98586 };
        contatoPaciente.paciente = paciente;

        activatedRoute.data = of({ contatoPaciente });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(contatoPaciente));
        expect(comp.pacientesSharedCollection).toContain(paciente);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const contatoPaciente = { id: 123 };
        spyOn(contatoPacienteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ contatoPaciente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contatoPaciente }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(contatoPacienteService.update).toHaveBeenCalledWith(contatoPaciente);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const contatoPaciente = new ContatoPaciente();
        spyOn(contatoPacienteService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ contatoPaciente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: contatoPaciente }));
        saveSubject.complete();

        // THEN
        expect(contatoPacienteService.create).toHaveBeenCalledWith(contatoPaciente);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const contatoPaciente = { id: 123 };
        spyOn(contatoPacienteService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ contatoPaciente });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(contatoPacienteService.update).toHaveBeenCalledWith(contatoPaciente);
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
