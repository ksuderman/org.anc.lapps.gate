package org.anc.lapps.gate;

import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.annotations.ServiceMetadata;

/**
 * @author Keith Suderman
 */
@ServiceMetadata(
        description = "Coreferencer from GATE",
        requires = { "person"},
        produces = { "http://vocab.lappsgrid.org/NamedEntity#matches" }
)
public class Coreferencer extends SimpleGateService
{
   public Coreferencer()
   {
      super(Coreferencer.class);
      createResource("gate.creole.coref.Coreferencer");
   }

//   public long[] produces()
//   {
//      return new long[] { Types.GATE, Types.COREF };
//   }
//
//   public long[] requires()
//   {
//      return new long[] { Types.GATE, Types.NAMED_ENTITES };
//   }

   @Override
   public String execute(String json)
   {
      Document document = null;
      try
      {
         document = doExecute(json);
      }
      catch (Exception e)
      {
         return DataFactory.error("Unable to execute the Coreferencer.", e);
      }
      if (document == null)
      {
         return DataFactory.error(UNEXPECTED);
      }
      String producer = this.getClass().getName() + "_" + Version.getVersion();
      FeatureMap features = document.getFeatures();
      Integer step = (Integer) features.get("lapps:step");
      if (step == null) {
         step = 1;
      }
      features.put("lapps:step", step + 1);
      features.put("lapps:PRONOMINAL_CORREFERNCE", step + " " + producer + " coref:gate");
//      features.put(Metadata.Contains.TYPE, "coref:gate");
//      features.put(Metadata.Contains.PRODUCER, producer);
//      features.put("annotation", Annotations.COREFERENCE);
      String result = DataFactory.gateDocument(document.toXml());
      Factory.deleteResource(document);
      return result;
   }

//   public Data getMetadata()
//   {
//      if (metadata == null)
//      {
//         try
//         {
//            String json = ResourceLoader.loadString("metadata/" + Coreferencer.class.getName() + ".json");
//            metadata = DataFactory.meta(json);
//         }
//         catch (IOException e)
//         {
//            metadata = DataFactory.error("Unable to load metadata json.", e);
//         }
//      }
//      return metadata;
//   }
}
