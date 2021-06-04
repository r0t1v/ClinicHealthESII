import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ResultadoExameDetailComponent } from './resultado-exame-detail.component';

describe('Component Tests', () => {
  describe('ResultadoExame Management Detail Component', () => {
    let comp: ResultadoExameDetailComponent;
    let fixture: ComponentFixture<ResultadoExameDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ResultadoExameDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ resultadoExame: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ResultadoExameDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ResultadoExameDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load resultadoExame on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.resultadoExame).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
