jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ResultadoExameService } from '../service/resultado-exame.service';
import { IResultadoExame, ResultadoExame } from '../resultado-exame.model';

import { ResultadoExameUpdateComponent } from './resultado-exame-update.component';

describe('Component Tests', () => {
  describe('ResultadoExame Management Update Component', () => {
    let comp: ResultadoExameUpdateComponent;
    let fixture: ComponentFixture<ResultadoExameUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let resultadoExameService: ResultadoExameService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ResultadoExameUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ResultadoExameUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ResultadoExameUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      resultadoExameService = TestBed.inject(ResultadoExameService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const resultadoExame: IResultadoExame = { id: 456 };

        activatedRoute.data = of({ resultadoExame });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(resultadoExame));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const resultadoExame = { id: 123 };
        spyOn(resultadoExameService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ resultadoExame });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: resultadoExame }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(resultadoExameService.update).toHaveBeenCalledWith(resultadoExame);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const resultadoExame = new ResultadoExame();
        spyOn(resultadoExameService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ resultadoExame });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: resultadoExame }));
        saveSubject.complete();

        // THEN
        expect(resultadoExameService.create).toHaveBeenCalledWith(resultadoExame);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const resultadoExame = { id: 123 };
        spyOn(resultadoExameService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ resultadoExame });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(resultadoExameService.update).toHaveBeenCalledWith(resultadoExame);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
