import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IContatoPaciente, ContatoPaciente } from '../contato-paciente.model';

import { ContatoPacienteService } from './contato-paciente.service';

describe('Service Tests', () => {
  describe('ContatoPaciente Service', () => {
    let service: ContatoPacienteService;
    let httpMock: HttpTestingController;
    let elemDefault: IContatoPaciente;
    let expectedResult: IContatoPaciente | IContatoPaciente[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ContatoPacienteService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        tipo: 'AAAAAAA',
        conteudo: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a ContatoPaciente', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ContatoPaciente()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ContatoPaciente', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            tipo: 'BBBBBB',
            conteudo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ContatoPaciente', () => {
        const patchObject = Object.assign({}, new ContatoPaciente());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ContatoPaciente', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            tipo: 'BBBBBB',
            conteudo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a ContatoPaciente', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addContatoPacienteToCollectionIfMissing', () => {
        it('should add a ContatoPaciente to an empty array', () => {
          const contatoPaciente: IContatoPaciente = { id: 123 };
          expectedResult = service.addContatoPacienteToCollectionIfMissing([], contatoPaciente);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contatoPaciente);
        });

        it('should not add a ContatoPaciente to an array that contains it', () => {
          const contatoPaciente: IContatoPaciente = { id: 123 };
          const contatoPacienteCollection: IContatoPaciente[] = [
            {
              ...contatoPaciente,
            },
            { id: 456 },
          ];
          expectedResult = service.addContatoPacienteToCollectionIfMissing(contatoPacienteCollection, contatoPaciente);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ContatoPaciente to an array that doesn't contain it", () => {
          const contatoPaciente: IContatoPaciente = { id: 123 };
          const contatoPacienteCollection: IContatoPaciente[] = [{ id: 456 }];
          expectedResult = service.addContatoPacienteToCollectionIfMissing(contatoPacienteCollection, contatoPaciente);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contatoPaciente);
        });

        it('should add only unique ContatoPaciente to an array', () => {
          const contatoPacienteArray: IContatoPaciente[] = [{ id: 123 }, { id: 456 }, { id: 52783 }];
          const contatoPacienteCollection: IContatoPaciente[] = [{ id: 123 }];
          expectedResult = service.addContatoPacienteToCollectionIfMissing(contatoPacienteCollection, ...contatoPacienteArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const contatoPaciente: IContatoPaciente = { id: 123 };
          const contatoPaciente2: IContatoPaciente = { id: 456 };
          expectedResult = service.addContatoPacienteToCollectionIfMissing([], contatoPaciente, contatoPaciente2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(contatoPaciente);
          expect(expectedResult).toContain(contatoPaciente2);
        });

        it('should accept null and undefined values', () => {
          const contatoPaciente: IContatoPaciente = { id: 123 };
          expectedResult = service.addContatoPacienteToCollectionIfMissing([], null, contatoPaciente, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(contatoPaciente);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
