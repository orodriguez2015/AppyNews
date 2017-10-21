package com.appynews.command.actions;

import android.view.View;

import com.appynews.adapter.FuenteDatosAdapter;
import com.appynews.command.api.OrigenRssMantenimientoApiCommand;
import com.appynews.controllers.OrigenRssController;
import com.appynews.dialog.AlertDialogHelper;
import com.appynews.exceptions.GetOrigenesRssException;
import com.appynews.model.dto.OrigenNoticiaVO;
import com.appynews.utils.LogCat;

import java.util.List;

import material.oscar.com.materialdesign.R;

/**
 * Clase que permite recuperar las fuentes de datos RSS a partir de las cuales se recuperan
 * las noticias
 *
 * @author <a href="mailto:oscar.rodriguezbrea@gmail.com">Óscar Rodríguez</a>
 */
public class GetFuentesDatosRssCommandAction implements CommandAction {

    private OrigenRssMantenimientoApiCommand apiCommand = null;
    private OrigenRssController controller = null;


    /**
     * Constructor
     * @param apiCommand OrigenRssMantenimientoApiCommand
     */
    public GetFuentesDatosRssCommandAction(OrigenRssMantenimientoApiCommand apiCommand) {
        this.apiCommand = apiCommand;
        this.controller = new OrigenRssController(apiCommand.getActivity());
    }

    @Override
    public Object execute() {
        List<OrigenNoticiaVO> origenes = null;

        try {

            origenes = controller.getOrigenes();

            LogCat.debug("cargarFuentesDatos origenes: " + origenes);
            FuenteDatosAdapter adapter = new FuenteDatosAdapter(origenes,apiCommand.getActivity());
            adapter.notifyDataSetChanged();

            /**
             * Se establece el listener que se pasa al adapter para que añade
             * este Listener a cada View a mostrar en el RecyclerView
             */
            adapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos =  apiCommand.getRecyclerView().getChildAdapterPosition(view);
                    LogCat.debug("Se ha seleccionado el elemento de la posición " + pos);
                }
            });

            apiCommand.getRecyclerView().setAdapter(adapter);


        } catch(GetOrigenesRssException e) {
            AlertDialogHelper.crearDialogoAlertaAdvertencia(apiCommand.getActivity(),apiCommand.getActivity().getString(R.string.atencion),apiCommand.getActivity().getString(R.string.err_get_fuentes_datos));
        } finally {
            return null;
        }

    }
}
