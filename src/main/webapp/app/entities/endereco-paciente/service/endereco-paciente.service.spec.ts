import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEnderecoPaciente, EnderecoPaciente } from '../endereco-paciente.model';

import { EnderecoPacienteService } from './endereco-paciente.service';

describe('Service Tests', () => {
  describe('EnderecoPaciente Service', () => {
    let service: EnderecoPacienteService;
    let httpMock: HttpTestingController;
    let elemDefault: IEnderecoPaciente;
    let expectedResult: IEnderecoPaciente | IEnderecoPaciente[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EnderecoPacienteService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        cidade: 'AAAAAAA',
        logradouro: 'AAAAAAA',
        bairro: 'AAAAAAA',
        numero: 0,
        referencia: 'AAAAAAA',
        cep: 'AAAAAAA',
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

      it('should create a EnderecoPaciente', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new EnderecoPaciente()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a EnderecoPaciente', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            cidade: 'BBBBBB',
            logradouro: 'BBBBBB',
            bairro: 'BBBBBB',
            numero: 1,
            referencia: 'BBBBBB',
            cep: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a EnderecoPaciente', () => {
        const patchObject = Object.assign(
          {
            cidade: 'BBBBBB',
            logradouro: 'BBBBBB',
            bairro: 'BBBBBB',
            referencia: 'BBBBBB',
          },
          new EnderecoPaciente()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of EnderecoPaciente', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            cidade: 'BBBBBB',
            logradouro: 'BBBBBB',
            bairro: 'BBBBBB',
            numero: 1,
            referencia: 'BBBBBB',
            cep: 'BBBBBB',
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

      it('should delete a EnderecoPaciente', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEnderecoPacienteToCollectionIfMissing', () => {
        it('should add a EnderecoPaciente to an empty array', () => {
          const enderecoPaciente: IEnderecoPaciente = { id: 123 };
          expectedResult = service.addEnderecoPacienteToCollectionIfMissing([], enderecoPaciente);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(enderecoPaciente);
        });

        it('should not add a EnderecoPaciente to an array that contains it', () => {
          const enderecoPaciente: IEnderecoPaciente = { id: 123 };
          const enderecoPacienteCollection: IEnderecoPaciente[] = [
            {
              ...enderecoPaciente,
            },
            { id: 456 },
          ];
          expectedResult = service.addEnderecoPacienteToCollectionIfMissing(enderecoPacienteCollection, enderecoPaciente);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a EnderecoPaciente to an array that doesn't contain it", () => {
          const enderecoPaciente: IEnderecoPaciente = { id: 123 };
          const enderecoPacienteCollection: IEnderecoPaciente[] = [{ id: 456 }];
          expectedResult = service.addEnderecoPacienteToCollectionIfMissing(enderecoPacienteCollection, enderecoPaciente);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(enderecoPaciente);
        });

        it('should add only unique EnderecoPaciente to an array', () => {
          const enderecoPacienteArray: IEnderecoPaciente[] = [{ id: 123 }, { id: 456 }, { id: 32310 }];
          const enderecoPacienteCollection: IEnderecoPaciente[] = [{ id: 123 }];
          expectedResult = service.addEnderecoPacienteToCollectionIfMissing(enderecoPacienteCollection, ...enderecoPacienteArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const enderecoPaciente: IEnderecoPaciente = { id: 123 };
          const enderecoPaciente2: IEnderecoPaciente = { id: 456 };
          expectedResult = service.addEnderecoPacienteToCollectionIfMissing([], enderecoPaciente, enderecoPaciente2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(enderecoPaciente);
          expect(expectedResult).toContain(enderecoPaciente2);
        });

        it('should accept null and undefined values', () => {
          const enderecoPaciente: IEnderecoPaciente = { id: 123 };
          expectedResult = service.addEnderecoPacienteToCollectionIfMissing([], null, enderecoPaciente, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(enderecoPaciente);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
