package org.anc.lapps.gate;

import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import org.lappsgrid.api.Data;
import org.lappsgrid.core.DataFactory;
import org.lappsgrid.discriminator.Types;
import org.lappsgrid.vocabulary.Annotations;
import org.lappsgrid.vocabulary.Metadata;

/**
 * @author Keith Suderman
 */
public class Gazetteer extends PooledGateService
{
   public Gazetteer()
   {
      super();
      createResource("gate.creole.gazetteer.DefaultGazetteer");
   }

   public long[] requires() {
      return new long[] { Types.GATE, Types.TOKEN };
   }

   // TODO Determine what annotation types are returned by the Gazetteer.
   public long[] produces() {
      return new long[] { Types.GATE, Types.TOKEN };
   }

   public Data execute(Data input)
   {
      Document document = null;
      try
      {
         document = doExecute(input);
      }
      catch (Exception e)
      {
         return DataFactory.error("Unable to execute the Coreferencer.", e);
      }
      if (document == null)
      {
         return DataFactory.error(BUSY);
      }
      String producer = this.getClass().getName() + "_" + Version.getVersion();
      FeatureMap features = Factory.newFeatureMap();
      features.put(Annotations.LOOKUP, producer + " lookup:gate");
//      features.put(Metadata.Contains.TYPE, "lookup:gate");
//      features.put(Metadata.Contains.PRODUCER, producer);
//      features.put("annotation", Annotations.LOOKUP);
      Data result = DataFactory.gateDocument(document.toXml());
      Factory.deleteResource(document);
      return result;
   }

}
