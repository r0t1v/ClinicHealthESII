import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'paciente',
        data: { pageTitle: 'clinicHealthSimpleApp.paciente.home.title' },
        loadChildren: () => import('./paciente/paciente.module').then(m => m.PacienteModule),
      },
      {
        path: 'endereco-paciente',
        data: { pageTitle: 'clinicHealthSimpleApp.enderecoPaciente.home.title' },
        loadChildren: () => import('./endereco-paciente/endereco-paciente.module').then(m => m.EnderecoPacienteModule),
      },
      {
        path: 'contato-paciente',
        data: { pageTitle: 'clinicHealthSimpleApp.contatoPaciente.home.title' },
        loadChildren: () => import('./contato-paciente/contato-paciente.module').then(m => m.ContatoPacienteModule),
      },
      {
        path: 'conta-clinica',
        data: { pageTitle: 'clinicHealthSimpleApp.contaClinica.home.title' },
        loadChildren: () => import('./conta-clinica/conta-clinica.module').then(m => m.ContaClinicaModule),
      },
      {
        path: 'tipo-convenio',
        data: { pageTitle: 'clinicHealthSimpleApp.tipoConvenio.home.title' },
        loadChildren: () => import('./tipo-convenio/tipo-convenio.module').then(m => m.TipoConvenioModule),
      },
      {
        path: 'exame',
        data: { pageTitle: 'clinicHealthSimpleApp.exame.home.title' },
        loadChildren: () => import('./exame/exame.module').then(m => m.ExameModule),
      },
      {
        path: 'medico',
        data: { pageTitle: 'clinicHealthSimpleApp.medico.home.title' },
        loadChildren: () => import('./medico/medico.module').then(m => m.MedicoModule),
      },
      {
        path: 'pagamento-exame',
        data: { pageTitle: 'clinicHealthSimpleApp.pagamentoExame.home.title' },
        loadChildren: () => import('./pagamento-exame/pagamento-exame.module').then(m => m.PagamentoExameModule),
      },
      {
        path: 'resultado-exame',
        data: { pageTitle: 'clinicHealthSimpleApp.resultadoExame.home.title' },
        loadChildren: () => import('./resultado-exame/resultado-exame.module').then(m => m.ResultadoExameModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
