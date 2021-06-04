import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ContatoPacienteDetailComponent } from './contato-paciente-detail.component';

describe('Component Tests', () => {
  describe('ContatoPaciente Management Detail Component', () => {
    let comp: ContatoPacienteDetailComponent;
    let fixture: ComponentFixture<ContatoPacienteDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ContatoPacienteDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ contatoPaciente: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ContatoPacienteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContatoPacienteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load contatoPaciente on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contatoPaciente).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
